module.exports = {
  link: [
    /** <link> tags for favicons **/
    { rel: "icon", type: "image/png", href: "/assets/icon/favicon.ico" },

    /** <link> tags for a Web App Manifest **/
    { rel: "manifest", href: "/assets/manifest.json" }
  ],
  meta: [
    { name: "theme-color", content: "#00bcd4" }
  ]
};
