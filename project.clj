(def libgdx-version "1.13.1")

(defproject clojure.gdx.backends.lwjgl "1.13.0-0.2"
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [com.badlogicgames.gdx/gdx-platform       ~libgdx-version :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-backend-lwjgl3 ~libgdx-version]]
  :plugins [[lein-codox "0.10.8"]]
  :codox {:source-uri "https://github.com/damn/clojure.gdx.backends.lwjgl/blob/main/{filepath}#L{line}"
          :metadata {:doc/format :markdown}}
  :global-vars {*warn-on-reflection* true})
