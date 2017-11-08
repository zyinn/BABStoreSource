/**
 * PostCSS
 * Reference: https://github.com/postcss/autoprefixer-core
 * Add vendor prefixes to your css
 */

module.exports = {
    plugins: {
        autoprefixer: {
            browsers: ['last 2 versions']
        },
    },
};