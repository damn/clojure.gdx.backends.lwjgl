# clojure.gdx.lwjgl

Clojure API for the libgdx lwjgl3 (desktop) backend

# Features

* Configures GLFW correctly for MacOS so the JVM argument `-XstartOnFirstThread` does _not_ have to be set.

* Option for setting taskbar-icon. This sets the dock-icon on mac and is not supported by the original library.

# Usage

* First you need to import libgdx dependency, this library will only bring in the dependencies of the backend, with leiningen this would be:

```clojure
:repositories [["jitpack" "https://jitpack.io"]]
:dependencies [[com.badlogicgames.gdx/gdx                   "1.13.0"]
               [com.github.damn/clojure.gdx.backends.lwjgl3 "1.13.0-0.1"]]
```

* Then just start the application by passing the config parameters and the lifecycle object.

```clojure
(ns my.application
  (:require [clojure.gdx.lwjgl :as lwjgl]))

(defn -main []
  (lwjgl/start {:title "Hello World"
                 :width 800
                 :height 600
                 :fps 60
                 :taskbar-icon "icon.png"} ; optional
                (reify lwjgl/Application
                  (create [_])
                  (dispose [_])
                  (render [_])
                  (resize [_ w h]))))
```

; TODO stupid readme code ! fails ! needs to be a runnable code not shit like that

also all the 'features' bla bla bla is just stupid

code is only the API that's it.
