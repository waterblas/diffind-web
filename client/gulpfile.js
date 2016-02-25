var gulpFilter = require('gulp-filter'),
    uglify = require('gulp-uglify'),
    cssmin = require('gulp-minify-css'),
    bowerSrc = require('gulp-bower-src'),
    sourcemaps = require('gulp-sourcemaps'),
    gutil = require('gulp-util'),
    rename = require("gulp-rename"),
    concat = require('gulp-concat');
    gulp = require('gulp');

var paths = {
    css: {
        files: ['src/css/*.css'],
        root: 'src/css'
    },
	js: ['src/js/*.js'],
    assets: ['src/img*/**','src/*.txt','src/*.html','src/font*/**','src/css*/filterable-list.css'],
    dest: './dist/'
};


// concat and minify CSS files
gulp.task('minify-css', function() {
    return gulp.src(paths.css.files)
        .pipe(cssmin({root:paths.css.root}))
        .pipe(gulp.dest(paths.dest+'css'));
});

// combine and minify js source files
gulp.task('scripts', function() {
    gulp.src(paths.js)
        .pipe(concat('all.js'))
        .pipe(rename('all.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest(paths.dest+'js'));
});


// copy main bower files (see bower.json) and optimize js
gulp.task('bower-files', function() {
    var filter = gulpFilter(["**/*.js", "!**/*.min.js"]);
    return bowerSrc()
        .pipe(sourcemaps.init())
        .pipe(filter)
        .pipe(uglify().on('error', gutil.log))
        .pipe(filter.restore())
        .pipe(sourcemaps.write("./"))
        .pipe(gulp.dest(paths.dest+'lib'));
});


// copy assets
gulp.task('copy-assets', function() {
    return gulp.src(paths.assets)
        .pipe(gulp.dest(paths.dest));
})

gulp.task('build', ['minify-css', 'scripts', 'bower-files', 'copy-assets'], function(){ });
