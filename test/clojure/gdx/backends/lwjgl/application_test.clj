(ns clojure.gdx.backends.lwjgl.application-test
  (:require [clojure.gdx.backends.lwjgl.application :as app]
            [clojure.gdx.backends.lwjgl.application.config :as config]))

(defn -main []
  (config/use-glfw-async!)
  (app/create (proxy [com.badlogic.gdx.ApplicationAdapter] [])
   (config/create {:title "foo"
                   :windowed-mode {:width 600 :height 500}
                   :foreground-fps 30
                   })
   )
  )
