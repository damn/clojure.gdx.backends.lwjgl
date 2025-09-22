(ns clojure.gdx.lwjgl-test
  (:require [clojure.gdx.backends.lwjgl :as lwjgl])
  (:import (com.badlogic.gdx ApplicationListener)
           (com.badlogic.gdx.graphics Color)
           (com.badlogic.gdx.utils SharedLibraryLoader
                                   Os)
           (org.lwjgl.system Configuration)))

(def empty-listener
  (reify ApplicationListener
    (create [_])
    (dispose [_])
    (render [_])
    (resize [_ width height])
    (pause [_])
    (resume [_])))

(defn- empty-test []
  (lwjgl/start-application! empty-listener {}))

(defn- full-screen-test []
  (let [display-mode (lwjgl/display-mode!)]
    (println "display-mode: " display-mode)
    (println "primary monitor: " (lwjgl/primary-monitor!))
    (lwjgl/start-application! empty-listener
                              {:fullscreen-mode display-mode}
                              )))

(defn- window-config-keys []
  (lwjgl/start-application! empty-listener
                            {:initial-visible? true
                             :windowed-mode {:width 100
                                             :height 100}
                             :resizable? true
                             :decorated? true
                             :maximized? false ; doesnt work, only when resizable
                             ; :maximized-monitor ; ?
                             :window-position {:x 0
                                               :y 0}
                             ; :window-size-limits
                             ; :window-icons
                             ; :window-listener
                             :initial-background-color Color/RED
                             :title "FOOX"
                             :vsync? false
                             }
                            ))

(defn -main []
  (when (= SharedLibraryLoader/os Os/MacOsX)
    (.set Configuration/GLFW_LIBRARY_NAME "glfw_async"))
  (empty-test)
  #_(full-screen-test)
  #_(window-config-keys)
  )
