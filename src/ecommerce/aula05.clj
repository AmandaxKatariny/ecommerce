(ns ecommerce.aula05
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(def conn (db/abre-conexao))

(db/cria-schema conn)

; não me importa como você extrai o MOMENTO da transação
; o que importa é vc usar esse momento pro seu as-of
; aqui no resultado da transacao eu podia usar o db-after
(let [computador (model/novo-produto "Computador Novo", "/computador-novo", 2500.10M)
      celular (model/novo-produto "Celular Caro", "/celular", 888888.10M)
      resultado @(d/transact conn [computador, celular])]
  (pprint resultado))

; meu snapshot, posso usar o momento real
(def fotografia-no-passado (d/db conn))

(let [calculadora {:produto/nome "Calculadora com 4 operações"}
      celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M)
      resultado @(d/transact conn [calculadora, celular-barato])]
  (pprint resultado))

; um snapshot no instante do d/db = 4
(pprint (count (db/todos-os-produtos (d/db conn))))

; rodando a query num banco filtrado com dados do passado = 2
(pprint (count (db/todos-os-produtos fotografia-no-passado)))


(println "\n Antes")
(pprint (count (db/todos-os-produtos (d/as-of (d/db conn) #inst "2021-12-09T19:05:10.290"))))

(println "\n no Meio")
(pprint (count (db/todos-os-produtos (d/as-of (d/db conn) #inst "2021-12-09T19:06:10.053"))))

(println "\n Depois")
(pprint (count (db/todos-os-produtos (d/as-of (d/db conn) #inst "2021-12-09T19:07:10.150"))))

;(db/apaga-banco)