(ns clojure.gdx.backends.lwjgl
  (:require [clojure.gdx.backends.lwjgl.application :as application]
            [clojure.gdx.backends.lwjgl.application.configuration :as config]))

(def start-application!            application/start!)
(def new-window!                   application/new-window!)
(def set-gl-debug-message-control! application/set-gl-debug-message-control!)
(def display-mode!                 config/display-mode!)
(def display-modes!                config/display-modes!)
(def primary-monitor!              config/primary-monitor!)
(def monitors!                     config/monitors!)
