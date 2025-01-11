

;;;;;;;;;;;;;; Lwjgl3WindowConfiguration

;  import java.util.Arrays;
;  import com.badlogic.gdx.Files.FileType;
;  import com.badlogic.gdx.Graphics;
;  import com.badlogic.gdx.Graphics.DisplayMode;
;  import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics.Lwjgl3DisplayMode;
;  import com.badlogic.gdx.graphics.Color;

{; the position of the window in windowed mode. Default -1 for both coordinates for centered on primary monitor.
  :window-x -1
  :window-y -1

  ; width the width of the window (default 640)
  :window-width 640
  ; height the height of the window (default 480)
  :window-height 480

  ; Sets minimum and maximum size limits for the window. If the window is full screen or not resizable, these limits are ignored. The default for all four parameters is -1, which means unrestricted.
  :window-min-width -1
  :windowMinHeight -1,
  :windowMaxWidth = -1,
  :windowMaxHeight = -1;

  ; whether the windowed mode window is resizable (default true)
  :window-resizable? true

  ; whether the windowed mode window is decorated, i.e. displaying the title bars (default true)
  :window-decorated? true

  ; whether the window starts maximized. Ignored if the window is full screen. (default false)
  :window-maximized? false

  ; what monitor the window should maximize to
  :maximized-monitor

  ; autoIconify whether the window should automatically iconify and restore previous video mode on input focus loss. (default true) Does nothing in windowed mode.
  :auto-iconify? true

  ; The type of file handle the paths are relative to. (default FileType#Internal)
  :window-icon-file-type

  ; Sets the icon that will be used in the window's title bar. Has no effect in macOS, which doesn't use window icons.
  ; One or more {@linkplain FileType#Internal internal} image paths. Must be JPEG, PNG, or BMP format. The one
  ;  	            closest to the system's desired size will be scaled. Good sizes include 16x16, 32x32 and 48x48.
  :window-icon-paths

  ; the {@link Lwjgl3WindowListener} which will be informed about iconficiation, focus loss and window close events.
  :window-listener

  ; Sets the app to use fullscreen mode. Use the static methods like {@link Lwjgl3ApplicationConfiguration#getDisplayMode()} on
	 ; * this class to enumerate connected monitors and their fullscreen display modes.
  :fullscreen-mode

  ; the window title. If null, the application listener's class name is used.
  :title (or given-title (.getSimpleName (class listener))) ; but in clojure we use the *ns* ?

  ; the initial background color. Defaults to black.
  :initial-background-color Color/BLACK

  ; visibility whether the window will be visible on creation. (default true)
  :initial-visible? true

; Sets whether to use vsync. This setting can be changed anytime at runtime via {@link Graphics#setVSync(boolean)}.
;  	 *
;  	 * For multi-window applications, only one (the main) window should enable vsync. Otherwise, every window will wait for the
;  	 * vertical blank on swap individually, effectively cutting the frame rate to (refreshRate / numberOfWindows).
  :vsync-enabled? true
  }


;;;;;;;;;;;;;; Lwjgl3ApplicationConfiguration

:gl-emulation/angle-gles20
:gl-emulation/gl20
:gl-emulation/gl30
:gl-emulation/gl31
:gl-emulation/gl32

{

 ; Whether to disable audio or not. If set to true, the returned audio class instances like {@link Audio} or {@link Music} will be mock implementations.
 :disable-audio? false
 ; The maximum number of threads to use for network requests. Default is {@link Integer#MAX_VALUE}.
 :max-net-threads Integer/MAX_VALUE
 :audioDeviceSimultaneousSources 16 ; the maximum number of sources that can be played simultaniously (default 16)
 :audioDeviceBufferSize 512 ; the audio device buffer size in samples
 :audioDeviceBufferCount 9 ; the audio device buffer count

; 	Sets which OpenGL version to use to emulate OpenGL ES. If the given major/minor version is not supported, the backend falls
; 	  back to OpenGL ES 2.0 emulation through OpenGL 2.0. The default parameters for major and minor should be 3 and 2
; 	  respectively to be compatible with Mac OS X. Specifying major version 4 and minor version 2 will ensure that all OpenGL ES
; 	  3.0 features are supported. Note however that Mac OS X does only support 3.2.
;
; 	  @see <a href= "http://legacy.lwjgl.org/javadoc/org/lwjgl/opengl/ContextAttribs.html"> LWJGL OSX ContextAttribs note</a>
;
; 	  @param glVersion which OpenGL ES emulation version to use
; 	  @param gles3MajorVersion OpenGL ES major version, use 3 as default
; 	  @param gles3MinorVersion OpenGL ES minor version, use 2 as default
 :gl-emulation :gl-emulation/gl20
 :gles30ContextMajorVersion 3
 :gles30ContextMinorVersion 2

 ; Sets the bit depth of the color, depth and stencil buffer as well as multi-sampling.
;	 * @param r red bits (default 8)
;	 * @param g green bits (default 8)
;	 * @param b blue bits (default 8)
;	 * @param a alpha bits (default 8)
;	 * @param depth depth bits (default 16)
;	 * @param stencil stencil bits (default 0)
;	 * @param samples MSAA samples (default 0) */
 :r 8 :g 8 :b 8 :a 8 :depth 16 :stencil 0 :samples 0


 ; Set transparent window hint. Results may vary on different OS and GPUs. Usage with the ANGLE backend is less consistent.
 :transparentFramebuffer ; boolean

 ; Sets the polling rate during idle time in non-continuous rendering mode. Must be positive. Default is 60.
 :idleFPS 60

 ; Sets the target framerate for the application. The CPU sleeps as needed. Must be positive. Use 0 to never sleep. Default is 0
 :foregroundFPS 0

 ; Sets whether to pause the application {@link ApplicationListener#pause()} and fire
 ; {@link LifecycleListener#pause()}/{@link LifecycleListener#resume()} events on when window is minimized/restored.
 :pauseWhenMinimized true

;	/** Sets whether to pause the application {@link ApplicationListener#pause()} and fire
;	 * {@link LifecycleListener#pause()}/{@link LifecycleListener#resume()} events on when window loses/gains focus. **/
 :pauseWhenLostFocus false

 ;	/** Sets the directory where {@link Preferences} will be stored, as well as the file type to be used to store them. Defaults to
 ;	 * "$USER_HOME/.prefs/" and {@link FileType#External}. */
 :preferencesDirectory ".prefs/"
 :preferencesFileType FileType$External


;	/** Defines how HDPI monitors are handled. Operating systems may have a per-monitor HDPI scale setting. The operating system
;	 * may report window width/height and mouse coordinates in a logical coordinate system at a lower resolution than the actual
;	 * physical resolution. This setting allows you to specify whether you want to work in logical or raw pixel units. See
;	 * {@link HdpiMode} for more information. Note that some OpenGL functions like {@link GL20#glViewport(int, int, int, int)} and
;	 * {@link GL20#glScissor(int, int, int, int)} require raw pixel units. Use {@link HdpiUtils} to help with the conversion if
;	 * HdpiMode is set to {@link HdpiMode#Logical}. Defaults to {@link HdpiMode#Logical}. */
 :hdpiMode HdpiMode$Logical


;	/** Enables use of OpenGL debug message callbacks. If not supported by the core GL driver (since GL 4.3), this uses the
;	 * KHR_debug, ARB_debug_output or AMD_debug_output extension if available. By default, debug messages with NOTIFICATION
;	 * severity are disabled to avoid log spam.
;	 *
;	 * You can call with {@link System#err} to output to the "standard" error output stream.
;	 *
;	 * Use {@link Lwjgl3Application#setGLDebugMessageControl(Lwjgl3Application.GLDebugMessageSeverity, boolean)} to enable or
;	 * disable other severity debug levels. */
 :debug? false
 :debug-stream System/err
 }
