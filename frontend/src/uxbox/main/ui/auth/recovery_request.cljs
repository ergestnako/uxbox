;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) 2015-2017 Andrey Antukh <niwi@niwi.nz>
;; Copyright (c) 2015-2017 Juan de la Cruz <delacruzgarciajuan@gmail.com>

(ns uxbox.main.ui.auth.recovery-request
  (:require [lentes.core :as l]
            [cuerdas.core :as str]
            [potok.core :as ptk]
            [uxbox.main.store :as st]
            [uxbox.main.data.auth :as uda]
            [uxbox.builtins.icons :as i]
            [uxbox.main.ui.messages :refer [messages-widget]]
            [uxbox.main.ui.navigation :as nav]
            [uxbox.util.router :as rt]
            [uxbox.util.forms :as forms]
            [uxbox.util.mixins :as mx :include-macros true]
            [uxbox.util.dom :as dom]))


(def form-data (forms/focus-data :recovery-request st/state))
(def set-value! (partial forms/set-value! st/store :recovery-request))

(def +recovery-request-form+
  {:username [forms/required forms/string]})

(mx/defc recovery-request-form
  {:mixins [mx/static mx/reactive]}
  []
  (let [data (mx/react form-data)
        valid? (forms/valid? data +recovery-request-form+)]
    (letfn [(on-change [field event]
              (let [value (dom/event->value event)]
                (set-value! field value)))
            (on-submit [event]
              (dom/prevent-default event)
              (st/emit! (uda/recovery-request data)
                        (forms/clear-form :recovery-request)
                        (forms/clear-errors :recovery-request)))]
      [:form {:on-submit on-submit}
       [:div.login-content
        [:input.input-text
         {:name "username"
          :value (:username data "")
          :on-change (partial on-change :username)
          :placeholder "username or email address"
          :type "text"}]
        [:input.btn-primary
         {:name "login"
          :class (when-not valid? "btn-disabled")
          :disabled (not valid?)
          :value "Recover password"
          :type "submit"}]
        [:div.login-links
         [:a {:on-click #(st/emit! (rt/navigate :auth/login))} "Go back!"]]]])))

;; --- Recovery Request Page

(mx/defc recovery-request-page
  {:mixins [mx/static (forms/clear-mixin st/store :recovery-request)]}
  []
  [:div.login
   [:div.login-body
    (messages-widget)
    [:a i/logo]
    (recovery-request-form)]])
