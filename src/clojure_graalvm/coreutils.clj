(ns clojure-graalvm.coreutils
  (:require [clojure.string :as string])
  (:import (java.io File)))

(defn ls
  [args]
  (let [dir (File. (str (or (get args :folder) ".")))
        options (get args :opts)
        files (if (get options :a) (.list dir) (filter #(not (string/starts-with? % ".")) (.list dir)))]

    (if (or (contains? options :h) (contains? options :help))
      (println "List directory contents\n-a\n  do not ignore hidden files/directories (starts with .)\n-l\n  use a long listing format")
      (let [result (if (get options :l)
                     (reduce (fn [acc cur]
                               (format "%s\n%s" cur acc)) "" files)
                     (reduce (fn [acc cur]
                               (format "%s %s" cur acc)) "\n" files))]
        (print result)
        )
      )
    )
  )


(defn catf
  [args]
  (let [fileName (first (get args :args))
        options (get args :opts)
        number (or (get options :n) (get options :number))]
    (print
      (if fileName
        (if number
          (reduce (fn [acc cur]

                    ) "" (line-seq fileName))
          (slurp fileName))
        "Missing file parameter"
        ))
    ))
