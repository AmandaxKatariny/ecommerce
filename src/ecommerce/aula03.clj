(ns ecommerce.aula03
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(def conn (db/abre-conexao))

(db/cria-schema conn)

(let [computador (model/novo-produto "Computador Novo", "/computador_novo", 2500.10M)
      celular-barato (model/novo-produto "Celular barato", "/celular-barato", 8879.00M)
      celular (model/novo-produto "Celular Caro", "/celular", 889400.10M)
      calculadora {:produto/nome "Calculadora com 4 operações"}]
(d/transact conn [celular-barato, celular, calculadora, celular-barato]))


(pprint (db/todos-os-produtos (d/db conn)))

(pprint (db/todos-os-produtos-por-slug-fixo (d/db conn)))

(pprint (db/todos-os-produtos-por-slug (d/db conn "/computador-novo")))


;(db/apaga-banco)
