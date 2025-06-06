(ns clojure.gdx.lwjgl-test
  (:require [clojure.gdx.backends.lwjgl :as lwjgl])
  (:import (com.badlogic.gdx ApplicationAdapter)
           (com.badlogic.gdx.graphics Color)
           (org.lwjgl.system Configuration)))

(defn- set-glfw-async! []
  (.set Configuration/GLFW_LIBRARY_NAME "glfw_async"))

(def empty-listener (proxy [ApplicationAdapter] []))

(defn- empty-test []
  (lwjgl/application {} empty-listener))

(defn- full-screen-test []
  (let [display-mode (lwjgl/display-mode)]
    (println "display-mode: " display-mode)
    (println "primary monitor: " (lwjgl/primary-monitor))
    (lwjgl/application {:fullscreen-mode display-mode}
                       empty-listener)))

(defn- window-config-keys []
  (lwjgl/application {:initial-visible? true
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
                     empty-listener))

(defn -main []
  (set-glfw-async!)

  #_(empty-test)

  #_(full-screen-test)

  #_(window-config-keys)

  ; TODO FIX REFLECTION WARNINGS


  )

(Lwjgl3Application. (doto (Lwjgl3ApplicationConfiguration.)
                      (.setBlubl )
                      (.setBlab)
                      ))

(lwjgl/application {:blub
                    :blab
                    })
