(ns cascalog-test.core
  (:require
    [cascalog.api :refer :all]
    [cascalog.playground :refer :all]
    [cascalog.logic.def :as def]
    [cascalog.logic.ops :as c]
    [cascalog.more-taps :refer [hfs-delimited]]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))



(def/defmapcatfn tokenise [line]
                 "reads in a line of string and splits it by a regular expression"
                 (clojure.string/split line #"[\[\]\\\(\),.)\s]+"))



(defn -main
  "I don't do a whole lot."
  []
  (?<- (stdout)
       [?word ?count]
       (sentence ?line)
       (tokenise ?line :> ?word)
       (c/count ?count)))


(comment
  sentence
  (type sentence)

  (?- (stdout)
      sentence)

  (?- (stdout)
      (<- [?line]
          (sentence :> ?line)))

  (?- (stdout)
      (<- [?word]
          (sentence :> ?line)
          (tokenise :< ?line :> ?word)))

  (?- (stdout)
      (<- [?word ?count]
          (sentence :> ?line)
          (tokenise :< ?line :> ?word)
          (c/count :> ?count)))

  (?<- (stdout)
       [?word ?count]
       (sentence ?line)
       (tokenise ?line :> ?word)
       (c/count ?count))

  (let [data-in "../../interviews/trainline/lastfm-dataset-1K/userid-timestamp-artid-artname-traid-traname_small.tsv"
        data-out "./"]
    (?- (hfs-delimited data-out :sinkmode :replace :delimiter ",")
        (total-sales-per-city
          (hfs-delimited data-in :delimiter ","))))

  (let [data-in "../../interviews/trainline/lastfm-dataset-1K/userid-timestamp-artid-artname-traid-traname_small.tsv"
        data-out "./"]
    (?<- (stdout)
        [?doc ?line]
        ((hfs-delimited data-in :skip-header? true) ?doc ?line)))
  )