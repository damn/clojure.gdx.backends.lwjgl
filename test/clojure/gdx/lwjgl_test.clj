(ns clojure.gdx.lwjgl-test
  (:require [clojure.gdx.backends.lwjgl :as lwjgl])
  (:import (com.badlogic.gdx ApplicationAdapter
                             Gdx)))

; TODO FIXME can't call display-mode without setting GLFW async first

(defn -main []
  #_(let [display-mode (lwjgl/display-mode)]
      (println "display-mode: " display-mode)
      (println "primary monitor: " (lwjgl/primary-monitor))
      (lwjgl/application {:fullscreen-mode display-mode
                          :mac-os {:glfw-async? true}}
                         (proxy [ApplicationAdapter] [])))

  (lwjgl/application {:mac-os {:glfw-async? true}
                      :resizable? true
                      :maximized? true


                      ;:opengl-emulation {:gl-version :angle-gles20
                      ;                   :gles-3-major-version 3
                      ;                   :gles-3-minor-version 2}

                      }
                     (proxy [ApplicationAdapter] [])))

(comment

 (lwjgl/new-window Gdx/app
                   (proxy [ApplicationAdapter] [])
                   {})

 )
