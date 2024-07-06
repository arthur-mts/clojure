(ns clojure-graalvm.coreutils
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.math :as math])
  (:import (java.io File LineNumberReader))
  (:import (java.nio.file Files)))

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

(defn generate-line-number [number size]
  (let [number-size (+ (math/floor (math/log10 number)) 1)
        blank-spaces (repeat (- size number-size) " ")]
    (string/join (concat blank-spaces [number])))
  )

(defn catf
  [args]
  (let [fileName (first (get args :args))
        options (get args :opts)
        number (or (get options :n) (get options :number))]
    (if fileName
      (if number
        (let [rdr (io/reader (File. (str fileName)))
              file (line-seq rdr)
              line-qnt (let [line-rdr (LineNumberReader. (io/reader (File. (str fileName))))]
                         (.skip line-rdr Long/MAX_VALUE)
                         (.getLineNumber line-rdr))
              line-qnt-size (+ (math/floor (math/log10 line-qnt)) 1)
              ]
          (reduce (fn [acc cur]
                    (println (format "  %s %s" (generate-line-number acc line-qnt-size) cur))
                    (+ acc 1))
                  1
                  file)
          )
        (println (slurp fileName))
        )
      (println "Missing file parameter"))
    ;(print
    ;  (if fileName
    ;    (if number
    ;      ;(with-open [rdr (io/reader (File. (str fileName)))
    ;      ;            file (doall (line-seq rdr))]
    ;      ;  "foo"
    ;      ;  )
    ;      (let [rdr (io/reader (File. (str fileName)))
    ;            file (doall (line-seq rdr))]
    ;        (reduce (fn [acc cur]
    ;                  ((println (format "  %d %s" acc cur))
    ;                   (+ acc 1))) 1 file))
    ;      (slurp fileName)
    ;      )
    ;    "Missing file parameter"
    ;    ))
    ))
