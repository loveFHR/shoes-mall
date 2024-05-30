module.exports = {
  // 环境:
  env: {
    browser: true,
    es2021: true,
    node: true
  },
  extends: [
    "plugin:vue/vue3-strongly-recommended",
    "prettier",
    "eslint:recommended",
    "plugin:prettier/recommended",
    "./.eslintrc-auto-import.json"
  ],
  parserOptions: {
    ecmaVersion: "13",
    sourceType: "module",
    requireConfigFile: false,
    parser: "@babel/eslint-parser",
    ecmaFeatures: {
      modules: true,
      jsx: true
    }
  },
  parser: "vue-eslint-parser",
  globals: {
    defineProps: "readonly",
    defineEmits: "readonly",
    defineExpose: "readonly",
    withDefault: "readonly"
  },
  plugins: ["vue", "prettier"],
  settings: {
    // 设置项目内的别名
    "import/reslover": {
      alias: {
        map: [["@", "./src"]]
      }
    },
    // 允许的扩展名
    "import/extensions": [".js", ".jsx", ".ts", ".tsx", ".mjs"]
  },
  rules: {
    "prettier/prettier": "error", // 开启 eslint 对 prettier 的支持
    semi: ["warn", "never"], // 禁止尾部使用分号
    "no-console": 0,
    "vue/valid-template-root": 0,
    "import/no-extraneous-dependencies": 0,
    "no-param-reassing": 0,
    "vue/multi-word-commponent-names": 0,
    "vue/attribute-hyphenation": 0,
    "vue/v-on-event-hyphenation": 0,
    "vue/multi-word-component-names": "off",
    "no-unused-vars": "off",
    "comma-dangle": [2, "never"] // 对象字面量项尾不能有逗号
  }
}
