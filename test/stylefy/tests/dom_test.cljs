(ns stylefy.tests.dom-test
  (:require [cljs.test :as test :refer-macros [deftest is testing]]
            [stylefy.core :as stylefy]
            [stylefy.impl.styles :as styles]
            [stylefy.impl.dom :as dom]
            [clojure.string :as str]))

(def simple-style {:padding "25px"
                   :background-color "#BBBBBB"
                   :border "1px solid black"})

(deftest simple-style->css
  (is (= (dom/style->css {:props simple-style :hash (styles/hash-style simple-style)}
                         {:pretty-print? false})
         "._stylefy_878532438{padding:25px;background-color:#BBBBBB;border:1px solid black}")))

(def clickable {:cursor :pointer})

(def autoprefix-style (merge {:border "1px solid black"
                              :border-radius "5px"
                              ::stylefy/vendors ["webkit" "moz" "o"]
                              ::stylefy/auto-prefix #{:border-radius}}
                             clickable))

(deftest autoprefixed-style->css
  (is (= (dom/style->css {:props autoprefix-style :hash (styles/hash-style autoprefix-style)}
                         {:pretty-print? false})
         "._stylefy_-216657570{border:1px solid black;border-radius:5px;-webkit-border-radius:5px;-moz-border-radius:5px;-o-border-radius:5px;cursor:pointer}")))

(def style-mode {::stylefy/mode {:hover {:background-color "#AAAAAA"}}})

(deftest mode-style->css
  (is (= (dom/style->css {:props style-mode :hash (styles/hash-style style-mode)}
                         {:pretty-print? false})
         "._stylefy_-2110434399{}._stylefy_-2110434399:hover{background-color:#AAAAAA}")))

(def responsive-style {:background-color "red"
                       ::stylefy/media {{:max-width "500px"} {:background-color "blue"}}})

(deftest responsive-style->css
  (is (= (dom/style->css {:props responsive-style :hash (styles/hash-style responsive-style)}
                         {:pretty-print? false})
         "._stylefy_1443051883{background-color:red}@media (max-width: 500px) {\n\n  ._stylefy_1443051883 {\n    background-color: blue;\n  }\n\n}")))