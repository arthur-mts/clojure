(ns clojure-graalvm.core)

(require '[babashka.cli :as cli])
(require '[clojure-graalvm.coreutils :as coreutils])

(defn help [m]
  (println
    (str "Clojure coreutils\nImplementation from some GNU Coreutils commands in Clojure\n"
         "Commands available:\n"
         "   - ls: List directory contents\n"
         "   - cat: Concatenate files and print on output")
    ))

(def cat-spec
  {:number {:alias   :n
            :require false
            :coerce  :boolean}})
(def command-table
  [{:cmds ["ls"] :fn coreutils/ls :args->opts [:folder]}
   {:cmds ["cat"] :fn coreutils/catf :spec cat-spec}
   {:cmds [] :fn help}
   ])

(defn -main [& args]
  (cli/dispatch command-table args))