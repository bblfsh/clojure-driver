# clojure-driver

### Example program input 

```clojure
(ns triangles.core
  (:require [clojure.string :refer :all]
            [clojure.java.io :refer :all]))

(defn to-int [s] 
  (Integer/parseInt s))

(defn is-triangle? [ln]
  (let [sides (map to-int (split (trim ln) #" +"))]
    (every? true? (map-indexed 
                    (fn [idx side]
                      (let [sides (take 3 (drop idx (cycle sides)))]
                        (> (reduce + (take 2 sides)) (last sides))))
                    sides))))

(defn -main []
  (with-open [rdr (reader "./data.txt")]
    (println (count (filter is-triangle? (line-seq rdr))))))
```

### Example output

```json
{
  "ast": [
    {
      "args": [
        {
          "class": "triangles.core",
          "form": "triangles.core",
          "op": "maybe-class"
        },
        {
          "args": [
            {
              "children": [
                "items"
              ],
              "form": [
                "clojure.string",
                "refer",
                "all"
              ],
              "items": [
                {
                  "class": "clojure.string",
                  "form": "clojure.string",
                  "op": "maybe-class"
                },
                {
                  "form": "refer",
                  "literal?": true,
                  "op": "const",
                  "type": "keyword",
                  "val": "refer"
                },
                {
                  "form": "all",
                  "literal?": true,
                  "op": "const",
                  "type": "keyword",
                  "val": "all"
                }
              ],
              "op": "vector"
            },
            {
              "children": [
                "items"
              ],
              "form": [
                "clojure.java.io",
                "refer",
                "all"
              ],
              "items": [
                {
                  "class": "clojure.java.io",
                  "form": "clojure.java.io",
                  "op": "maybe-class"
                },
                {
                  "form": "refer",
                  "literal?": true,
                  "op": "const",
                  "type": "keyword",
                  "val": "refer"
                },
                {
                  "form": "all",
                  "literal?": true,
                  "op": "const",
                  "type": "keyword",
                  "val": "all"
                }
              ],
              "op": "vector"
            }
          ],
          "children": [
            "fn",
            "args"
          ],
          "fn": {
            "form": "require",
            "literal?": true,
            "op": "const",
            "type": "keyword",
            "val": "require"
          },
          "form": [
            "require",
            [
              "clojure.string",
              "refer",
              "all"
            ],
            [
              "clojure.java.io",
              "refer",
              "all"
            ]
          ],
          "op": "invoke"
        }
      ],
      "children": [
        "fn",
        "args"
      ],
      "fn": {
        "class": "ns",
        "form": "ns",
        "op": "maybe-class"
      },
      "form": [
        "ns",
        "triangles.core",
        [
          "require",
          [
            "clojure.string",
            "refer",
            "all"
          ],
          [
            "clojure.java.io",
            "refer",
            "all"
          ]
        ]
      ],
      "op": "invoke",
      "top-level": true
    },
    {
      "args": [
        {
          "class": "to-int",
          "form": "to-int",
          "op": "maybe-class"
        },
        {
          "children": [
            "items"
          ],
          "form": [
            "s"
          ],
          "items": [
            {
              "class": "s",
              "form": "s",
              "op": "maybe-class"
            }
          ],
          "op": "vector"
        },
        {
          "args": [
            {
              "class": "s",
              "form": "s",
              "op": "maybe-class"
            }
          ],
          "children": [
            "fn",
            "args"
          ],
          "fn": {
            "class": "Integer",
            "field": "parseInt",
            "form": "Integer/parseInt",
            "op": "maybe-host-form"
          },
          "form": [
            "Integer/parseInt",
            "s"
          ],
          "op": "invoke"
        }
      ],
      "children": [
        "fn",
        "args"
      ],
      "fn": {
        "class": "defn",
        "form": "defn",
        "op": "maybe-class"
      },
      "form": [
        "defn",
        "to-int",
        [
          "s"
        ],
        [
          "Integer/parseInt",
          "s"
        ]
      ],
      "op": "invoke",
      "top-level": true
    },
    {
      "args": [
        {
          "class": "is-triangle?",
          "form": "is-triangle?",
          "op": "maybe-class"
        },
        {
          "children": [
            "items"
          ],
          "form": [
            "ln"
          ],
          "items": [
            {
              "class": "ln",
              "form": "ln",
              "op": "maybe-class"
            }
          ],
          "op": "vector"
        },
        {
          "args": [
            {
              "children": [
                "items"
              ],
              "form": [
                "sides",
                [
                  "map",
                  "to-int",
                  [
                    "split",
                    [
                      "trim",
                      "ln"
                    ],
                    " +"
                  ]
                ]
              ],
              "items": [
                {
                  "class": "sides",
                  "form": "sides",
                  "op": "maybe-class"
                },
                {
                  "args": [
                    {
                      "class": "to-int",
                      "form": "to-int",
                      "op": "maybe-class"
                    },
                    {
                      "args": [
                        {
                          "args": [
                            {
                              "class": "ln",
                              "form": "ln",
                              "op": "maybe-class"
                            }
                          ],
                          "children": [
                            "fn",
                            "args"
                          ],
                          "fn": {
                            "class": "trim",
                            "form": "trim",
                            "op": "maybe-class"
                          },
                          "form": [
                            "trim",
                            "ln"
                          ],
                          "op": "invoke"
                        },
                        {
                          "form": " +",
                          "literal?": true,
                          "op": "const",
                          "type": "regex",
                          "val": " +"
                        }
                      ],
                      "children": [
                        "fn",
                        "args"
                      ],
                      "fn": {
                        "class": "split",
                        "form": "split",
                        "op": "maybe-class"
                      },
                      "form": [
                        "split",
                        [
                          "trim",
                          "ln"
                        ],
                        " +"
                      ],
                      "op": "invoke"
                    }
                  ],
                  "children": [
                    "fn",
                    "args"
                  ],
                  "fn": {
                    "class": "map",
                    "form": "map",
                    "op": "maybe-class"
                  },
                  "form": [
                    "map",
                    "to-int",
                    [
                      "split",
                      [
                        "trim",
                        "ln"
                      ],
                      " +"
                    ]
                  ],
                  "op": "invoke"
                }
              ],
              "op": "vector"
            },
            {
              "args": [
                {
                  "class": "true?",
                  "form": "true?",
                  "op": "maybe-class"
                },
                {
                  "args": [
                    {
                      "args": [
                        {
                          "children": [
                            "items"
                          ],
                          "form": [
                            "idx",
                            "side"
                          ],
                          "items": [
                            {
                              "class": "idx",
                              "form": "idx",
                              "op": "maybe-class"
                            },
                            {
                              "class": "side",
                              "form": "side",
                              "op": "maybe-class"
                            }
                          ],
                          "op": "vector"
                        },
                        {
                          "args": [
                            {
                              "children": [
                                "items"
                              ],
                              "form": [
                                "sides",
                                [
                                  "take",
                                  3,
                                  [
                                    "drop",
                                    "idx",
                                    [
                                      "cycle",
                                      "sides"
                                    ]
                                  ]
                                ]
                              ],
                              "items": [
                                {
                                  "class": "sides",
                                  "form": "sides",
                                  "op": "maybe-class"
                                },
                                {
                                  "args": [
                                    {
                                      "form": 3,
                                      "literal?": true,
                                      "op": "const",
                                      "type": "number",
                                      "val": 3
                                    },
                                    {
                                      "args": [
                                        {
                                          "class": "idx",
                                          "form": "idx",
                                          "op": "maybe-class"
                                        },
                                        {
                                          "args": [
                                            {
                                              "class": "sides",
                                              "form": "sides",
                                              "op": "maybe-class"
                                            }
                                          ],
                                          "children": [
                                            "fn",
                                            "args"
                                          ],
                                          "fn": {
                                            "class": "cycle",
                                            "form": "cycle",
                                            "op": "maybe-class"
                                          },
                                          "form": [
                                            "cycle",
                                            "sides"
                                          ],
                                          "op": "invoke"
                                        }
                                      ],
                                      "children": [
                                        "fn",
                                        "args"
                                      ],
                                      "fn": {
                                        "class": "drop",
                                        "form": "drop",
                                        "op": "maybe-class"
                                      },
                                      "form": [
                                        "drop",
                                        "idx",
                                        [
                                          "cycle",
                                          "sides"
                                        ]
                                      ],
                                      "op": "invoke"
                                    }
                                  ],
                                  "children": [
                                    "fn",
                                    "args"
                                  ],
                                  "fn": {
                                    "class": "take",
                                    "form": "take",
                                    "op": "maybe-class"
                                  },
                                  "form": [
                                    "take",
                                    3,
                                    [
                                      "drop",
                                      "idx",
                                      [
                                        "cycle",
                                        "sides"
                                      ]
                                    ]
                                  ],
                                  "op": "invoke"
                                }
                              ],
                              "op": "vector"
                            },
                            {
                              "args": [
                                {
                                  "args": [
                                    {
                                      "class": "+",
                                      "form": "+",
                                      "op": "maybe-class"
                                    },
                                    {
                                      "args": [
                                        {
                                          "form": 2,
                                          "literal?": true,
                                          "op": "const",
                                          "type": "number",
                                          "val": 2
                                        },
                                        {
                                          "class": "sides",
                                          "form": "sides",
                                          "op": "maybe-class"
                                        }
                                      ],
                                      "children": [
                                        "fn",
                                        "args"
                                      ],
                                      "fn": {
                                        "class": "take",
                                        "form": "take",
                                        "op": "maybe-class"
                                      },
                                      "form": [
                                        "take",
                                        2,
                                        "sides"
                                      ],
                                      "op": "invoke"
                                    }
                                  ],
                                  "children": [
                                    "fn",
                                    "args"
                                  ],
                                  "fn": {
                                    "class": "reduce",
                                    "form": "reduce",
                                    "op": "maybe-class"
                                  },
                                  "form": [
                                    "reduce",
                                    "+",
                                    [
                                      "take",
                                      2,
                                      "sides"
                                    ]
                                  ],
                                  "op": "invoke"
                                },
                                {
                                  "args": [
                                    {
                                      "class": "sides",
                                      "form": "sides",
                                      "op": "maybe-class"
                                    }
                                  ],
                                  "children": [
                                    "fn",
                                    "args"
                                  ],
                                  "fn": {
                                    "class": "last",
                                    "form": "last",
                                    "op": "maybe-class"
                                  },
                                  "form": [
                                    "last",
                                    "sides"
                                  ],
                                  "op": "invoke"
                                }
                              ],
                              "children": [
                                "fn",
                                "args"
                              ],
                              "fn": {
                                "class": "\u003e",
                                "form": "\u003e",
                                "op": "maybe-class"
                              },
                              "form": [
                                "\u003e",
                                [
                                  "reduce",
                                  "+",
                                  [
                                    "take",
                                    2,
                                    "sides"
                                  ]
                                ],
                                [
                                  "last",
                                  "sides"
                                ]
                              ],
                              "op": "invoke"
                            }
                          ],
                          "children": [
                            "fn",
                            "args"
                          ],
                          "fn": {
                            "class": "let",
                            "form": "let",
                            "op": "maybe-class"
                          },
                          "form": [
                            "let",
                            [
                              "sides",
                              [
                                "take",
                                3,
                                [
                                  "drop",
                                  "idx",
                                  [
                                    "cycle",
                                    "sides"
                                  ]
                                ]
                              ]
                            ],
                            [
                              "\u003e",
                              [
                                "reduce",
                                "+",
                                [
                                  "take",
                                  2,
                                  "sides"
                                ]
                              ],
                              [
                                "last",
                                "sides"
                              ]
                            ]
                          ],
                          "op": "invoke"
                        }
                      ],
                      "children": [
                        "fn",
                        "args"
                      ],
                      "fn": {
                        "class": "fn",
                        "form": "fn",
                        "op": "maybe-class"
                      },
                      "form": [
                        "fn",
                        [
                          "idx",
                          "side"
                        ],
                        [
                          "let",
                          [
                            "sides",
                            [
                              "take",
                              3,
                              [
                                "drop",
                                "idx",
                                [
                                  "cycle",
                                  "sides"
                                ]
                              ]
                            ]
                          ],
                          [
                            "\u003e",
                            [
                              "reduce",
                              "+",
                              [
                                "take",
                                2,
                                "sides"
                              ]
                            ],
                            [
                              "last",
                              "sides"
                            ]
                          ]
                        ]
                      ],
                      "op": "invoke"
                    },
                    {
                      "class": "sides",
                      "form": "sides",
                      "op": "maybe-class"
                    }
                  ],
                  "children": [
                    "fn",
                    "args"
                  ],
                  "fn": {
                    "class": "map-indexed",
                    "form": "map-indexed",
                    "op": "maybe-class"
                  },
                  "form": [
                    "map-indexed",
                    [
                      "fn",
                      [
                        "idx",
                        "side"
                      ],
                      [
                        "let",
                        [
                          "sides",
                          [
                            "take",
                            3,
                            [
                              "drop",
                              "idx",
                              [
                                "cycle",
                                "sides"
                              ]
                            ]
                          ]
                        ],
                        [
                          "\u003e",
                          [
                            "reduce",
                            "+",
                            [
                              "take",
                              2,
                              "sides"
                            ]
                          ],
                          [
                            "last",
                            "sides"
                          ]
                        ]
                      ]
                    ],
                    "sides"
                  ],
                  "op": "invoke"
                }
              ],
              "children": [
                "fn",
                "args"
              ],
              "fn": {
                "class": "every?",
                "form": "every?",
                "op": "maybe-class"
              },
              "form": [
                "every?",
                "true?",
                [
                  "map-indexed",
                  [
                    "fn",
                    [
                      "idx",
                      "side"
                    ],
                    [
                      "let",
                      [
                        "sides",
                        [
                          "take",
                          3,
                          [
                            "drop",
                            "idx",
                            [
                              "cycle",
                              "sides"
                            ]
                          ]
                        ]
                      ],
                      [
                        "\u003e",
                        [
                          "reduce",
                          "+",
                          [
                            "take",
                            2,
                            "sides"
                          ]
                        ],
                        [
                          "last",
                          "sides"
                        ]
                      ]
                    ]
                  ],
                  "sides"
                ]
              ],
              "op": "invoke"
            }
          ],
          "children": [
            "fn",
            "args"
          ],
          "fn": {
            "class": "let",
            "form": "let",
            "op": "maybe-class"
          },
          "form": [
            "let",
            [
              "sides",
              [
                "map",
                "to-int",
                [
                  "split",
                  [
                    "trim",
                    "ln"
                  ],
                  " +"
                ]
              ]
            ],
            [
              "every?",
              "true?",
              [
                "map-indexed",
                [
                  "fn",
                  [
                    "idx",
                    "side"
                  ],
                  [
                    "let",
                    [
                      "sides",
                      [
                        "take",
                        3,
                        [
                          "drop",
                          "idx",
                          [
                            "cycle",
                            "sides"
                          ]
                        ]
                      ]
                    ],
                    [
                      "\u003e",
                      [
                        "reduce",
                        "+",
                        [
                          "take",
                          2,
                          "sides"
                        ]
                      ],
                      [
                        "last",
                        "sides"
                      ]
                    ]
                  ]
                ],
                "sides"
              ]
            ]
          ],
          "op": "invoke"
        }
      ],
      "children": [
        "fn",
        "args"
      ],
      "fn": {
        "class": "defn",
        "form": "defn",
        "op": "maybe-class"
      },
      "form": [
        "defn",
        "is-triangle?",
        [
          "ln"
        ],
        [
          "let",
          [
            "sides",
            [
              "map",
              "to-int",
              [
                "split",
                [
                  "trim",
                  "ln"
                ],
                " +"
              ]
            ]
          ],
          [
            "every?",
            "true?",
            [
              "map-indexed",
              [
                "fn",
                [
                  "idx",
                  "side"
                ],
                [
                  "let",
                  [
                    "sides",
                    [
                      "take",
                      3,
                      [
                        "drop",
                        "idx",
                        [
                          "cycle",
                          "sides"
                        ]
                      ]
                    ]
                  ],
                  [
                    "\u003e",
                    [
                      "reduce",
                      "+",
                      [
                        "take",
                        2,
                        "sides"
                      ]
                    ],
                    [
                      "last",
                      "sides"
                    ]
                  ]
                ]
              ],
              "sides"
            ]
          ]
        ]
      ],
      "op": "invoke",
      "top-level": true
    },
    {
      "args": [
        {
          "class": "-main",
          "form": "-main",
          "op": "maybe-class"
        },
        {
          "children": [
            "items"
          ],
          "form": [],
          "items": [],
          "op": "vector"
        },
        {
          "args": [
            {
              "children": [
                "items"
              ],
              "form": [
                "rdr",
                [
                  "reader",
                  "./data.txt"
                ]
              ],
              "items": [
                {
                  "class": "rdr",
                  "form": "rdr",
                  "op": "maybe-class"
                },
                {
                  "args": [
                    {
                      "form": "./data.txt",
                      "literal?": true,
                      "op": "const",
                      "type": "string",
                      "val": "./data.txt"
                    }
                  ],
                  "children": [
                    "fn",
                    "args"
                  ],
                  "fn": {
                    "class": "reader",
                    "form": "reader",
                    "op": "maybe-class"
                  },
                  "form": [
                    "reader",
                    "./data.txt"
                  ],
                  "op": "invoke"
                }
              ],
              "op": "vector"
            },
            {
              "args": [
                {
                  "args": [
                    {
                      "args": [
                        {
                          "class": "is-triangle?",
                          "form": "is-triangle?",
                          "op": "maybe-class"
                        },
                        {
                          "args": [
                            {
                              "class": "rdr",
                              "form": "rdr",
                              "op": "maybe-class"
                            }
                          ],
                          "children": [
                            "fn",
                            "args"
                          ],
                          "fn": {
                            "class": "line-seq",
                            "form": "line-seq",
                            "op": "maybe-class"
                          },
                          "form": [
                            "line-seq",
                            "rdr"
                          ],
                          "op": "invoke"
                        }
                      ],
                      "children": [
                        "fn",
                        "args"
                      ],
                      "fn": {
                        "class": "filter",
                        "form": "filter",
                        "op": "maybe-class"
                      },
                      "form": [
                        "filter",
                        "is-triangle?",
                        [
                          "line-seq",
                          "rdr"
                        ]
                      ],
                      "op": "invoke"
                    }
                  ],
                  "children": [
                    "fn",
                    "args"
                  ],
                  "fn": {
                    "class": "count",
                    "form": "count",
                    "op": "maybe-class"
                  },
                  "form": [
                    "count",
                    [
                      "filter",
                      "is-triangle?",
                      [
                        "line-seq",
                        "rdr"
                      ]
                    ]
                  ],
                  "op": "invoke"
                }
              ],
              "children": [
                "fn",
                "args"
              ],
              "fn": {
                "class": "println",
                "form": "println",
                "op": "maybe-class"
              },
              "form": [
                "println",
                [
                  "count",
                  [
                    "filter",
                    "is-triangle?",
                    [
                      "line-seq",
                      "rdr"
                    ]
                  ]
                ]
              ],
              "op": "invoke"
            }
          ],
          "children": [
            "fn",
            "args"
          ],
          "fn": {
            "class": "with-open",
            "form": "with-open",
            "op": "maybe-class"
          },
          "form": [
            "with-open",
            [
              "rdr",
              [
                "reader",
                "./data.txt"
              ]
            ],
            [
              "println",
              [
                "count",
                [
                  "filter",
                  "is-triangle?",
                  [
                    "line-seq",
                    "rdr"
                  ]
                ]
              ]
            ]
          ],
          "op": "invoke"
        }
      ],
      "children": [
        "fn",
        "args"
      ],
      "fn": {
        "class": "defn",
        "form": "defn",
        "op": "maybe-class"
      },
      "form": [
        "defn",
        "-main",
        [],
        [
          "with-open",
          [
            "rdr",
            [
              "reader",
              "./data.txt"
            ]
          ],
          [
            "println",
            [
              "count",
              [
                "filter",
                "is-triangle?",
                [
                  "line-seq",
                  "rdr"
                ]
              ]
            ]
          ]
        ]
      ],
      "op": "invoke",
      "top-level": true
    }
  ],
  "driver": "clojure:1.0.0",
  "errors": [],
  "language": "clojure",
  "language_version": "1.0.0",
  "status": "ok"
}
```

