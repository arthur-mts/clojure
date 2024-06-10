(ns script
  (:import (java.net URI)
           (java.util UUID)))
(require '[clojure.repl :refer :all])


;Funções multi-arity
(defn hi
  ([] (hi "stranger"))
  ([name] (str "Hello " name))
)

(prn (hi))

;Funções variadicas
(defn um_e_o_resto [um & resto]
  (println "Um " um " e o resto " resto))

(prn (um_e_o_resto 1 "asd" "FDOO" 4 5))

; Declaração de valor
(def um 1)

(prn um)


;; Transformando um valor em um vetor
(prn (vector 1))


; Utilizando o caractere ' a expressão não é 'evaluada'
(prn '(um_e_o_resto 1 2))

;; let permite transformar valores em simbolos para serem utilizados em um bloco
(let [first 1
      second 2]
  (prn first "-" second))

;; Usando código java
(prn (str(UUID/randomUUID)))

;; https://clojure.org/guides/learn/functions
;1
(defn greet [] (prn "Hello"))
(greet)
;2
(def greet (fn [] (prn "Hello")))
(greet)
(def greet #(prn "Hello"))
(greet)
;3
(defn greeting
  ([] (greeting "Hello World!"))
  ([x] (greeting "Hello" x))
  ([x, y] (str x ", " y "!"))
)

(prn (greeting "Jhon"))
; 4
(defn do-nothing [x] (x))

;5
(defn always-thing
  ([x & y]  100)
)

(prn (always-thing 10 10 0110))

;6
(defn make-thingy [x] (fn
                        ([] x)
                        ([y] x)
                        ([y & z] x)
))
(let [n (rand-int Integer/MAX_VALUE)
      f (make-thingy n)]
  (assert (= n (f)))
  (assert (= n (f 123)))
  (assert (= n (apply f 123 (range)))))

;7
(defn triplicate [f]
  (f)
  (f)
  (f)
)

(triplicate (fn [] (println "OI")))

;8
(defn opposite [f]
  (fn [& args] (not(apply f args))))

(println ((opposite (fn [] true))))

;9
(defn triplicate2 [f & args] (triplicate (fn [] (apply f args))))

(triplicate2 (fn [& args] (println (apply str args))) '(1 2))

; 10
(println (Math/cos Math/PI))

; identidade do seno
(println ((fn [x] (+
          (Math/pow (Math/sin x) 2)
          (Math/pow (Math/cos x) 2)
          )
   ) 10)
 )

;11
(defn http-get [url] (slurp (.openStream
                       (.toURL (URI.(str url)))
                     ))
)

(assert (.contains (http-get "https://www.w3.org") "html"))
;(println (http-get "https://www.w3.org"))

;12
(defn one-less-arg [f x] (fn [& args] (apply f x args)))

(def only-one-arg (one-less-arg (fn [x y z] (println (str x "-" y "-" z))) 1))

(only-one-arg 2 3)

;13
(defn two-fns [f g]
  (fn [x]
    (let [res-g (g x)]
      (f res-g)
    )
  )
)