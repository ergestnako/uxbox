(ns uxbox.shapes)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Types
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:static ^:private +hierarchy+
  (as-> (make-hierarchy) $
    (derive $ :builtin/icon ::shape)
    (derive $ :builtin/icon-svg ::shape)
    (derive $ :builtin/icon-group ::shape)))

(defn shape?
  [type]
  {:pre [(keyword? type)]}
  (isa? +hierarchy+ type ::shape))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Api
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(declare -render)

(defn render
  ([shape] (-render shape nil))
  ([shape attrs] (-render shape attrs)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Implementation Api
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- dispatch-by-type
  [shape & params]
  (:type shape))

(defmulti -render
  dispatch-by-type
  :hierarchy #'+hierarchy+)

(defmulti -move
  dispatch-by-type
  :hierarchy #'+hierarchy+)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Implementation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmethod -move ::shape
  [shape {:keys [dx dy] :as opts}]
  (assoc shape
         :x (+ (:x shape) dx)
         :y (+ (:y shape) dy)))