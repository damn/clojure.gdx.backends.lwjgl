(ns clojure.gdx.lwjgl-test
  (:require [clojure.app :as app]
            [clojure.gdx.backends.lwjgl :as lwjgl]))

(defn -main []
  (.set org.lwjgl.system.Configuration/GLFW_LIBRARY_NAME "glfw_async")
  (lwjgl/application (reify app/Listener
                       (create [_])
                       (dispose [_])
                       (pause [_])
                       (render [_])
                       (resize [_ width height])
                       (resume [_]))))
