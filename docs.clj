(ns clojure.gdx.backends.lwjgl
  "Clojure API for `com.badlogic.gdx.backends.lwjgl3`.

  * Application Options

  | Key                | Description | Default value |
  | --------           | -------     | -------       |
  | `:audio`    | Sets the audio device configuration. <br> Parameters: <br> `:simultaneous-sources` - the maximum number of sources that can be played simultaniously <br> `:buffer-size` - the audio device buffer size in samples <br> `:buffer-count` - the audio device buffer count | `{:simultaneous-sources 16,` <br> `:buffer-size 512,` <br> `:buffer-count 9}` |
   | `:disable-audio?`  | Whether to disable audio or not. If set to true, the returned audio class instances like `Audio` or `Music` will be mock implementations.    | `false` |
   | `:max-net-threads` | Sets the maximum number of threads to use for network requests.     | `Integer/MAX_VALUE` |
  | `:opengl-emulation`    |  Sets which OpenGL version to use to emulate OpenGL ES. If the given major/minor version is not supported, the backend falls back to OpenGL ES 2.0 emulation through OpenGL 2.0. The default parameters for major and minor should be 3 and 2 respectively to be compatible with Mac OS X. Specifying major version 4 and minor version 2 will ensure that all OpenGL ES 3.0 features are supported. Note however that Mac OS X does only support 3.2. <br> <br> Parameters: <br> `:gl-version` - which OpenGL ES emulation version to use (Options are: `:gl-emulation/angle-gles20`, `:gl-emulation/gl20`, `:gl-emulation/gl31` and `:gl-emulation/gl32`. <br> `:gles-3-major-version` - OpenGL ES major version <br> `:gles-3-minor-version` - OpenGL ES minor version <br> See Also: <br> https://legacy.lwjgl.org/javadoc/org/lwjgl/opengl/ContextAttribs.html   | `{:gl-version :gl-emulation/gl20,` <br> `:gles-3-major-version 3,` <br> `:gles-3-minor-version 2}` |
  | `:backbuffer`    | Sets the bit depth of the color, depth and stencil buffer as well as multi-sampling. <br> <br> Parameters: <br> `:r` - red bits <br> `:g` - green bits <br> `:b` - blue bits <br> `:a` - alpha bits <br> `:depth` - depth bits <br> `:stencil` - stencil bits <br> `:samples` - MSAA samples | `{:r 8,` <br> `:g 8,` <br> `:b 8,` <br> `:a 8,` <br> `:depth 16,` <br> `:stencil 0,` <br> `:samples 0}`|
  | `:transparent-framebuffer`    | Set transparent window hint. Results may vary on different OS and GPUs. Usage with the ANGLE backend is less consistent.    | `false` |
  | `:idle-fps`    | Sets the polling rate during idle time in non-continuous rendering mode. Must be positive.     | `60` |
  | `:foreground-fps`    | Sets the target framerate for the application. The CPU sleeps as needed. Must be positive. Use 0 to never sleep.   | `0` |
  | `:pause-when-minimized?`    | Sets whether to pause the application `ApplicationListener.pause()` and fire `LifecycleListener.pause()`/`LifecycleListener.resume()` events on when window is minimized/restored.    | `true` |
  | `:pause-when-lost-focus?`    | Sets whether to pause the application `ApplicationListener.pause()` and fire `LifecycleListener.pause()/LifecycleListener.resume()` events on when window loses/gains focus.    | `false` |
  | `:preferences`    | Sets the directory where Preferences will be stored, as well as the file type to be used to store them.  | `{:directory \"$USER_HOME/.prefs/\"` <br> `:files/file-type Files.FileType.External}`|
  | `:hdpi-mode`    | Defines how HDPI monitors are handled. Operating systems may have a per-monitor HDPI scale setting. The operating system may report window width/height and mouse coordinates in a logical coordinate system at a lower resolution than the actual physical resolution. This setting allows you to specify whether you want to work in logical or raw pixel units. See `HdpiMode` for more information. Note that some OpenGL functions like `GL20.glViewport(int, int, int, int)` and `GL20.glScissor(int, int, int, int)` require raw pixel units. Use `HdpiUtils` to help with the conversion if `HdpiMode` is set to `HdpiMode.Logical.`   | `HdpiMode.Logical` |
  | `:gl-debug-output?`    | Enables use of OpenGL debug message callbacks. If not supported by the core GL driver (since GL 4.3), this uses the KHR_debug, ARB_debug_output or AMD_debug_output extension if available. By default, debug messages with NOTIFICATION severity are disabled to avoid log spam. You can call with System.err to output to the \"standard\" error output stream. Use [[set-gl-debug-message-control]] to enable or disable other severity debug levels.    | |

  * Window Options

  | Key                         | Description | Default value |
  | ----------------------------| ----------  | ------------  |
  | `:initial-visible?`         | whether the window will be visible on creation | `true` |
  | `:windowed-mode`            | Sets the app to use windowed mode. |  `{:width 640 :height 480}` |
  | `:resizable?`               | whether the windowed mode window is resizable | `true` |
  | `:decorated?`               |  whether the windowed mode window is decorated, i.e. displaying the title bars | `true` |
  | `:maximized?`               | whether the window starts maximized. Ignored if the window is full screen.  | `false` |
  | `maximized-monitor`         | what monitor the window should maximize to | - |
  | `:auto-iconify?`            | whether the window should automatically iconify and restore previous video mode on input focus loss. Does nothing in windowed mode. | `true` |
  | `:window-position`          |  Sets the position of the window in windowed mode. Default -1 for both coordinates for centered on primary monitor. | `{:x -1 :y -1}` |
  | `:window-size-limits`       | Sets minimum and maximum size limits for the window. If the window is full screen or not resizable, these limits are ignored. The default for all four parameters is -1, which means unrestricted. | `{:min-width -1` <br> `:min-height -1` <br> `:max-width -1` <br> `:max-height -1}` |
  | `:window-icon`              | Sets the icon that will be used in the window's title bar. Has no effect in macOS, which doesn't use window icons. | `` |
  | `:windowed-listener`        | Sets the {@link Lwjgl3WindowListener} which will be informed about iconficiation, focus loss and window close events. | `` |
  | `:fullscreen-mode`          |  Sets the app to use fullscreen mode. Use the static methods like {@link Lwjgl3ApplicationConfiguration#getDisplayMode()} on this class to enumerate connected monitors and their fullscreen display modes. | |
  | `:title`                    | Sets the window title. If null, the application listener's class name is used.  | `` |
  | `:initial-background-color` | Sets the initial background color. Defaults to black.  | `` |
  | `:vsync?`                   | Sets whether to use vsync. This setting can be changed anytime at runtime via {@link Graphics#setVSync(boolean)}. For multi-window applications, only one (the main) window should enable vsync. Otherwise, every window will wait for the vertical blank on swap individually, effectively cutting the frame rate to (refreshRate / numberOfWindows). | `` |
  "
  (:require [clojure.gdx.application.listener :as listener]
            [clojure.gdx.backends.lwjgl.application :as application]
            [clojure.gdx.backends.lwjgl.application.configuration :as application-configuration]

            [clojure.gdx.backends.lwjgl.application.gl-debug-message-severity :as gl-debug-message-severity]
            [clojure.gdx.backends.lwjgl.graphics.display-mode :as display-mode]
            [clojure.gdx.backends.lwjgl.graphics.monitor :as monitor]
            [clojure.gdx.backends.lwjgl.window.configuration :as window-configuration]))

(def display-mode!
  "The currently active display-mode of the primary or given monitor.

  Initializes GLFW if not initialized.

  Example:

  `{:width 1440, :height 900, :refresh-rate 60, :bits-per-pixel 24, :monitor-handle 140207961116912}`"
  config/display-mode!)

(def display-modes!
  "The available display-modes of the primary or given monitor.

  Initializes GLFW if not initialized."
  config/display-modes!)

(def primary-monitor!
  "The primary `Graphics.Monitor`.

  Initializes GLFW if not initialized.

  Example:

  `{:virtual-x 0, :virtual-y 0, :name \"Built-in Retina Display\", :monitor-handle 140655926337712}`"
  config/primary-monitor!)

(def monitors!
  "The connected `Graphics.Monitors`.

  Initializes GLFW if not initialized."
  config/monitors!)

(defn start-application!
  "Starts a `com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application` with the given listener and config.

  Listener is a map of functions:
  ```
    {:create!  (fn [context])
     :dispose! (fn [])
     :render!  (fn [])
     :resize!  (fn [width height])
     :resume!  (fn [])
     :pause!   (fn [])}
  ```

  `context` is a map with the global static fields of `com.badlogic.gdx.Gdx`:

  ```
  {:app      Gdx/app
   :graphics Gdx/graphics
   :audio    Gdx/audio
   :input    Gdx/input
   :files    Gdx/files
   :net      Gdx/net
   :gl       Gdx/gl
   :gl20     Gdx/gl20
   :gl30     Gdx/gl30
   :gl31     Gdx/gl31
   :gl32     Gdx/gl32}
  ```

  `config` can contain both application and window configuration options as mentioned in the namespace docs."
  [config listener]
  (application/start! (listener/create listener)
                      (config/create config)))

(defn new-window!
  "Creates a new `com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window` using the provided `com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener` listener and config.

  Config should contain only window options.

  This function only just instantiates a `Lwjgl3Window` and returns immediately. The actual window creation is postponed with `Application.postRunnable(Runnable)` until after all existing windows are updated."
  [application listener config]
  (application/new-window! application
                           listener
                           (window-configuration/create config)))

(defn set-gl-debug-message-control!
  "Enables or disables GL debug messages for the specified severity level. Returns false if the severity level could not be set (e.g. the NOTIFICATION level is not supported by the ARB and AMD extensions). See `Lwjgl3ApplicationConfiguration.enableGLDebugOutput(boolean, PrintStream)`."
  [severity enabled?]
  (application/set-gl-debug-message-control! severity enabled?))
