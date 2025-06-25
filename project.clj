(def libgdx-version "1.13.5")

(defproject clojure.gdx.backends.lwjgl "1.13.5"

  ; for dev loop
  :repositories [["jitpack" "https://jitpack.io"]]

  :dependencies [[org.clojure/clojure "1.12.0"]
                 [com.badlogicgames.gdx/gdx-platform       ~libgdx-version :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-backend-lwjgl3 ~libgdx-version]

                 [com.github.damn/clojure.dev-loop "ef54a03"]
                 ]
  :plugins [[lein-codox "0.10.8"]]
  :codox {:source-uri "https://github.com/damn/clojure.gdx.backends.lwjgl/blob/main/{filepath}#L{line}"
          :metadata {:doc/format :markdown}}
  :global-vars {*warn-on-reflection* true})
