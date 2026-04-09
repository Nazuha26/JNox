#ifndef WINVER
#define WINVER 0x0A00   // Supported Windows 10 and higher
#endif

#include <dwmapi.h>
#include <jawt.h>
#include <jawt_md.h>
#include <jni.h>
#include <windows.h>
#include <windowsx.h>

#include "WinNativeLib.h"



namespace Utils
{
    struct WindowData {
        WNDPROC originalWndProc;
        int titleBarHeight;
        int captionButtonsWidth;
        COLORREF bgColor;
        int minWidth;
        int minHeight;
    };



    int getTopResizeHandleHeight(HWND hwnd)
    {
        const UINT dpi = GetDpiForWindow(hwnd);
        return GetSystemMetricsForDpi(SM_CXPADDEDBORDER, dpi) + GetSystemMetricsForDpi(SM_CYSIZEFRAME, dpi);
    }



    /**
     * Updates the window's frame margins and corner rounding.
     */
    void UpdateDwmAppearance(HWND hwnd)
    {
        constexpr MARGINS margins = { 0, 0, 0, 0 };
        if (FAILED(DwmExtendFrameIntoClientArea(hwnd, &margins)))
        {
            /* log */
        }

        const int cornerPreference = IsZoomed(hwnd) ? DWMWCP_DONOTROUND : DWMWCP_ROUNDSMALL;
        if (FAILED(
            DwmSetWindowAttribute(hwnd, DWMWA_WINDOW_CORNER_PREFERENCE, &cornerPreference, sizeof(cornerPreference))))
        {
            /* log */
        };
    }



    HWND GetHwndFromJavaComponent(JNIEnv* env, jobject component)
    {
        if (!component)
            return nullptr;

        JAWT awt;
        awt.version = JAWT_VERSION_1_4;
        if (JAWT_GetAWT(env, &awt) == JNI_FALSE)
            return nullptr;

        JAWT_DrawingSurface* ds = awt.GetDrawingSurface(env, component);
        if (ds == nullptr)
            return nullptr;

        HWND hwnd = nullptr;
        jint lock = ds->Lock(ds);
        if ((lock & JAWT_LOCK_ERROR) == 0)
        {
            JAWT_DrawingSurfaceInfo* dsi = ds->GetDrawingSurfaceInfo(ds);
            if (dsi != nullptr)
            {
                JAWT_Win32DrawingSurfaceInfo* dsi_win = static_cast<JAWT_Win32DrawingSurfaceInfo*>(dsi->platformInfo);
                if (dsi_win != nullptr)
                    hwnd = dsi_win->hwnd;
                ds->FreeDrawingSurfaceInfo(dsi);
            }
            ds->Unlock(ds);
        }
        awt.FreeDrawingSurface(ds);
        return hwnd;
    }



    /**
     * Receive and handle native windows commands
     * @note Macros CALLBACK uses for Windows 10 (1607) support on 32-bit CPUs
     */
    LRESULT CALLBACK CustomWndProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam)
    {
        WindowData* data = static_cast<WindowData*>(GetPropW(hwnd, L"NoxWinData"));
        if (!data) { return DefWindowProcW(hwnd, msg, wParam, lParam); }

        switch (msg)
        {
            // Windows size has changed
            case WM_SIZE: {
                LRESULT res = CallWindowProcW(data->originalWndProc, hwnd, msg, wParam, lParam);
                UpdateDwmAppearance(hwnd);
                return res;
            }

            // Size and position of a window's client area must be calculated
            // Restore full client area without system title bar
            case WM_NCCALCSIZE: {
                if (wParam != TRUE) { return CallWindowProcW(data->originalWndProc, hwnd, msg, wParam, lParam); }

                NCCALCSIZE_PARAMS* p = reinterpret_cast<NCCALCSIZE_PARAMS*>(lParam);
                const RECT original = p->rgrc[0];

                CallWindowProcW(data->originalWndProc, hwnd, msg, wParam, lParam);

                p->rgrc[0] = original;

                if (IsZoomed(hwnd))
                {
                    const UINT dpi = GetDpiForWindow(hwnd);
                    const int frameX = GetSystemMetricsForDpi(SM_CXSIZEFRAME, dpi) + GetSystemMetricsForDpi(
                        SM_CXPADDEDBORDER, dpi);
                    const int frameY = GetSystemMetricsForDpi(SM_CYSIZEFRAME, dpi) + GetSystemMetricsForDpi(
                        SM_CXPADDEDBORDER, dpi);

                    p->rgrc[0].top += frameY;
                    p->rgrc[0].left += frameX;
                    p->rgrc[0].right -= frameX;
                    p->rgrc[0].bottom -= frameY;
                }
                return 0;
            }

            // Helps determine which part of a window corresponds to a specific screen coordinate
            // Custom size and caption handling
            case WM_NCHITTEST: {
                const LRESULT def = CallWindowProcW(data->originalWndProc, hwnd, msg, wParam, lParam);
                if (def != HTCLIENT)
                    return def;

                RECT rw { };
                GetWindowRect(hwnd, &rw);
                const int globPhysX = GET_X_LPARAM(lParam);
                const int globPhysY = GET_Y_LPARAM(lParam);
                const int x = globPhysX - rw.left;
                const int y = globPhysY - rw.top;
                const int windowWidth = rw.right - rw.left;
                const int windowHeight = rw.bottom - rw.top;

                const bool thick = (GetWindowLongW(hwnd, GWL_STYLE) & WS_THICKFRAME) != 0;

                if (thick && !IsZoomed(hwnd))
                {
                    const UINT dpi = GetDpiForWindow(hwnd);
                    const int frameX = GetSystemMetricsForDpi(SM_CXSIZEFRAME, dpi) + GetSystemMetricsForDpi(
                        SM_CXPADDEDBORDER, dpi);
                    const int frameY = GetSystemMetricsForDpi(SM_CYSIZEFRAME, dpi) + GetSystemMetricsForDpi(
                        SM_CXPADDEDBORDER, dpi);

                    bool isLeft = x < frameX;
                    bool isRight = x >= windowWidth - frameX;
                    bool isTop = y < frameY;
                    bool isBottom = y >= windowHeight - frameY;

                    if (isTop && isLeft)
                        return HTTOPLEFT;
                    if (isTop && isRight)
                        return HTTOPRIGHT;
                    if (isBottom && isLeft)
                        return HTBOTTOMLEFT;
                    if (isBottom && isRight)
                        return HTBOTTOMRIGHT;
                    if (isLeft)
                        return HTLEFT;
                    if (isRight)
                        return HTRIGHT;
                    if (isTop)
                        return HTTOP;
                    if (isBottom)
                        return HTBOTTOM;
                }
                else if (thick && IsZoomed(hwnd))
                {
                    if (y < getTopResizeHandleHeight(hwnd))
                        return HTTOP;
                }

                if (y < data->titleBarHeight && x < (windowWidth - data->captionButtonsWidth)) { return HTCAPTION; }

                return HTCLIENT;
            }

            // Sent to a window when the size or position of the window is about to change.
            // Override the window's default minimum tracking size.
            case WM_GETMINMAXINFO: {
                CallWindowProcW(data->originalWndProc, hwnd, msg, wParam, lParam);

                MINMAXINFO* mmi = reinterpret_cast<MINMAXINFO*>(lParam);

                UINT dpi = GetDpiForWindow(hwnd);
                double scale = static_cast<double>(dpi) / 96.0;

                mmi->ptMinTrackSize.x = static_cast<int>(data->minWidth * scale);
                mmi->ptMinTrackSize.y = static_cast<int>(data->minHeight * scale);

                return 0;
            }

            // Window background must be erased
            // Repaint native background
            case WM_ERASEBKGND: {
                HDC hdc = reinterpret_cast<HDC>(wParam);
                RECT rc;
                GetClientRect(hwnd, &rc);

                HBRUSH brush = CreateSolidBrush(data->bgColor);
                FillRect(hdc, &rc, brush);
                DeleteObject(brush);
                return 1;
            }

            // Notifies a window that its nonclient area is being destroyed
            case WM_NCDESTROY: {
                WNDPROC origProc = data->originalWndProc;
                RemovePropW(hwnd, L"NoxWinData");
                delete data;
                SetWindowLongPtrW(hwnd, GWLP_WNDPROC, reinterpret_cast<LONG_PTR>(origProc));
                return CallWindowProcW(origProc, hwnd, msg, wParam, lParam);
            }

            default: return CallWindowProcW(data->originalWndProc, hwnd, msg, wParam, lParam);
        }
    }
}



extern "C" {
    JNIEXPORT void JNICALL Java_io_github_nazuha26_WinNativeLib_hookWindow(JNIEnv* env, jobject, jobject component)
    {
        HWND hwnd = Utils::GetHwndFromJavaComponent(env, component);
        if (!hwnd || GetPropW(hwnd, L"NoxWinData"))
            return;

        Utils::WindowData* data = new Utils::WindowData { nullptr, 32, 96, RGB(255, 255, 255), 256, 32}; // NOLINT
        data->originalWndProc = reinterpret_cast<WNDPROC>(SetWindowLongPtrW(
            hwnd, GWLP_WNDPROC, reinterpret_cast<LONG_PTR>(&Utils::CustomWndProc)));

        SetPropW(hwnd, L"NoxWinData", data);

        Utils::UpdateDwmAppearance(hwnd);
        SetWindowPos(hwnd, nullptr, 0, 0, 0, 0, SWP_NOMOVE | SWP_NOSIZE | SWP_NOZORDER | SWP_FRAMECHANGED);
    }



    JNIEXPORT void JNICALL Java_io_github_nazuha26_WinNativeLib_configureWindow(
        JNIEnv* env, jobject, jobject component, jint titleBarHeight, jint captionButtonsWidth, jboolean isResizable)
    {
        HWND hwnd = Utils::GetHwndFromJavaComponent(env, component);
        if (!hwnd) return;

        LONG_PTR style = GetWindowLongPtrW(hwnd, GWL_STYLE);
        style |= WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX;

        if (isResizable) { style |= WS_THICKFRAME | WS_MAXIMIZEBOX; }
        else
        {
            style &= ~WS_THICKFRAME;
            style &= ~WS_MAXIMIZEBOX;
        }
        SetWindowLongPtrW(hwnd, GWL_STYLE, style);

        // Получаем данные для обновления
        Utils::WindowData* data = static_cast<Utils::WindowData*>(GetPropW(hwnd, L"NoxWinData"));
        if (data)
        {
            UINT dpi = GetDpiForWindow(hwnd);
            double scale = static_cast<double>(dpi) / 96.0;

            data->titleBarHeight = static_cast<int>(titleBarHeight * scale);
            data->captionButtonsWidth = static_cast<int>(captionButtonsWidth * scale);
        }

        SetWindowPos(hwnd, nullptr, 0, 0, 0, 0, SWP_NOMOVE | SWP_NOSIZE | SWP_NOZORDER | SWP_FRAMECHANGED);
    }



    JNIEXPORT jdoubleArray JNICALL Java_io_github_nazuha26_WinNativeLib_getDPIScale(
        JNIEnv* env, jobject, jobject component)
    {
        HWND hwnd = Utils::GetHwndFromJavaComponent(env, component);
        double scale = 1.0;
        if (hwnd)
        {
            UINT dpi = GetDpiForWindow(hwnd);
            scale = static_cast<double>(dpi) / 96.0;
        }
        jdoubleArray result = env->NewDoubleArray(2);
        if (result == nullptr)
            return nullptr;
        double scales[2] = { scale, scale };
        env->SetDoubleArrayRegion(result, 0, 2, scales);
        return result;
    }



    JNIEXPORT void JNICALL Java_io_github_nazuha26_WinNativeLib_minimizeWindow(JNIEnv* env, jobject, jobject component)
    {
        HWND hwnd = Utils::GetHwndFromJavaComponent(env, component);
        if (hwnd)
            PostMessageW(hwnd, WM_SYSCOMMAND, SC_MINIMIZE, 0);
    }

    JNIEXPORT void JNICALL Java_io_github_nazuha26_WinNativeLib_maximizeWindow(JNIEnv* env, jobject, jobject component)
    {
        HWND hwnd = Utils::GetHwndFromJavaComponent(env, component);
        if (hwnd)
            PostMessageW(hwnd, WM_SYSCOMMAND, SC_MAXIMIZE, 0);
    }

    JNIEXPORT void JNICALL Java_io_github_nazuha26_WinNativeLib_restoreWindow(JNIEnv* env, jobject, jobject component)
    {
        HWND hwnd = Utils::GetHwndFromJavaComponent(env, component);
        if (hwnd)
            PostMessageW(hwnd, WM_SYSCOMMAND, SC_RESTORE, 0);
    }

    JNIEXPORT void JNICALL Java_io_github_nazuha26_WinNativeLib_closeWindow(JNIEnv* env, jobject, jobject component)
    {
        HWND hwnd = Utils::GetHwndFromJavaComponent(env, component);
        if (hwnd)
            PostMessageW(hwnd, WM_CLOSE, 0, 0);
    }



    JNIEXPORT void JNICALL Java_io_github_nazuha26_WinNativeLib_unhookWindow(JNIEnv* env, jobject, jobject component)
    {
        HWND hwnd = Utils::GetHwndFromJavaComponent(env, component);
        Utils::WindowData* data = static_cast<Utils::WindowData*>(GetPropW(hwnd, L"NoxWinData"));
        if (data)
        {
            SetWindowLongPtrW(hwnd, GWLP_WNDPROC, reinterpret_cast<LONG_PTR>(data->originalWndProc));
            RemovePropW(hwnd, L"NoxWinData");
            delete data;
            SetWindowPos(hwnd, nullptr, 0, 0, 0, 0, SWP_NOMOVE | SWP_NOSIZE | SWP_NOZORDER | SWP_FRAMECHANGED);
        }
    }



    JNIEXPORT void JNICALL Java_io_github_nazuha26_WinNativeLib_setBackgroundColor(
        JNIEnv* env, jobject, jobject component, jint r, jint g, jint b)
    {
        HWND hwnd = Utils::GetHwndFromJavaComponent(env, component);
        if (!hwnd)
            return;

        Utils::WindowData* data = reinterpret_cast<Utils::WindowData*>(GetPropW(hwnd, L"NoxWinData"));

        if (data)
        {
            data->bgColor = RGB(r, g, b);
            InvalidateRect(hwnd, nullptr, TRUE);   // Send WM_ERASEBKGND and WM_NCPAINT
        }
    }



    /**
     * @note Supported Windows 11 and higher only
     */
    JNIEXPORT void JNICALL Java_io_github_nazuha26_WinNativeLib_setBorderColor(
        JNIEnv* env, jobject, jobject component, jint r, jint g, jint b)
    {
        HWND hwnd = Utils::GetHwndFromJavaComponent(env, component);
        if (!hwnd)
            return;

        const COLORREF color = RGB(r, g, b);

        DwmSetWindowAttribute(hwnd, DWMWA_BORDER_COLOR, &color, sizeof(color));
    }

    JNIEXPORT void JNICALL Java_io_github_nazuha26_WinNativeLib_setMinSize(
        JNIEnv* env, jobject, jobject component, jint minWidth, jint minHeight)
    {
        HWND hwnd = Utils::GetHwndFromJavaComponent(env, component);
        if (!hwnd)
            return;

        Utils::WindowData* data = static_cast<Utils::WindowData*>(GetPropW(hwnd, L"NoxWinData"));
        if (data)
        {
            data->minWidth = static_cast<int>(minWidth);
            data->minHeight = static_cast<int>(minHeight);
        }
    }
}