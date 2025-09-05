(ns clojure.gdx.backends.lwjgl
  (:require [clojure.gdx.application.listener :as listener]
            [clojure.gdx.backends.lwjgl.application :as application]
            [clojure.gdx.backends.lwjgl.application.configuration :as config]
            [clojure.gdx.backends.lwjgl.window.configuration :as window-config]))

(def display-mode!    config/display-mode!)
(def display-modes!   config/display-modes!)
(def primary-monitor! config/primary-monitor!)
(def monitors!        config/monitors!)

(defn start-application! [listener config]
  (application/start! (listener/create listener)
                      (config/create config)))

(defn new-window! [application listener config]
  (application/new-window! application
                           listener
                           (window-config/create config)))

(defn set-gl-debug-message-control! [severity enabled?]
  (application/set-gl-debug-message-control! severity enabled?))
