(ns script)
(require '[clojure.repl :refer :all])

(defn hi [name] (str "Hello " name))

(prn (hi "Arthur"))

(doc +)