(def build-folder "target")

(def jar-content (str build-folder "/classes"))

(def lib-name 'my.library/app')
(def version "0.0.1")
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file-name (format "%s/%s-%s.jar" build-folder (name lib-name) version))

(def app-name "application_name")
(def uber-file-name (format "%s/%s-%s-standalone.jar" build-folder app-name version))

(defn clean [_]
  (b/delete {:path build-folder})
  (println (format "Build folder \"%s\" removed" build-folder)))

(defn jar [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir jar-content})

  (b/write-pom {:class-dir jar-content
                :lib lib-name
                :version version
                :basis basis
                :src-dirs ["src"]})
  (b/jar {:class-dir jar-content
          :jar-file jar-file-name})

  (println (format "Jar file created: \"%s\"" jar-file-name)))

(defn uber [_]
  (clean nil)

  ;; Resources if need
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir jar-content})

  (b/compile-clj {:basis basis
                  :src-dirs ["src"]
                  :ns-compile '[app.main]
                  :class-dir jar-content})
  (b/uber {:class-dir jar-content
           :uber-file uber-file-name
           :basis basis
           :main 'app.main'})
  (println (format "Uber file crated: \"%s\"" uber-file-name)))
