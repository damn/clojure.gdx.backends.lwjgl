(ns clojure.gdx.lwjgl-test
  (:require [clojure.gdx.backends.lwjgl :as lwjgl]))

(defn -main []
  (.set org.lwjgl.system.Configuration/GLFW_LIBRARY_NAME "glfw_async")
  (lwjgl/application (proxy [com.badlogic.gdx.ApplicationAdapter] []
                       (create [])
                       (dispose [])
                       (render [])
                       (resize [width height]))))





