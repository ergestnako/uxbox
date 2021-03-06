;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) 2016 Andrey Antukh <niwi@niwi.nz>

(ns uxbox.main.ui.shapes.icon
  (:require [uxbox.main.ui.shapes.common :as common]
            [uxbox.main.ui.shapes.attrs :as attrs]
            [uxbox.main.geom :as geom]
            [uxbox.util.mixins :as mx :include-macros true]
            [uxbox.util.geom.matrix :as gmt]
            [uxbox.util.geom.point :as gpt]))

;; --- Icon Component

(declare icon-shape)

(mx/defc icon-component
  {:mixins [mx/static mx/reactive]}
  [{:keys [id] :as shape}]
  (let [modifiers (mx/react (common/modifiers-ref id))
        selected (mx/react common/selected-ref)
        selected? (contains? selected id)
        on-mouse-down #(common/on-mouse-down % shape selected)
        shape (assoc shape :modifiers modifiers)]
    [:g.shape {:class (when selected? "selected")
               :on-mouse-down on-mouse-down}
     (icon-shape shape)]))

;; --- Icon Shape

(mx/defc icon-shape
  {:mixins [mx/static]}
  [{:keys [id content metadata rotation x1 y1 modifiers] :as shape}]
  (let [{:keys [width height]} (geom/size shape)
        {:keys [resize displacement]} modifiers

        view-box (apply str (interpose " " (:view-box metadata)))

        xfmt (cond-> (gmt/matrix)
               resize (gmt/multiply resize)
               displacement (gmt/multiply displacement)
               rotation (gmt/rotate* rotation (gpt/point (+ x1 (/ width 2))
                                                         (+ y1 (/ height 2)))))
        props {:id (str id)
               :x x1
               :y y1
               :view-box view-box
               :width width
               :height height
               :preserve-aspect-ratio "none"
               :dangerouslySetInnerHTML {:__html content}}

        attrs (merge props (attrs/extract-style-attrs shape))]
    [:g {:transform (str xfmt)}
     [:svg attrs]]))

;; --- Icon SVG

(mx/defc icon-svg
  {:mixins [mx/static]}
  [{:keys [content id metadata] :as shape}]
  (let [view-box (apply str (interpose " " (:view-box metadata)))
        props {:view-box view-box
               :id (str "shape-" id)
               :dangerouslySetInnerHTML {:__html content}}]
    [:svg props]))
