var gulp = require('gulp');
var browserify = require('browserify');
var babelify = require('babelify');
var connect = require('gulp-connect');
var sourcemaps = require('gulp-sourcemaps');
var source = require('vinyl-source-stream');
var del = require('del');
var runSequence = require('run-sequence');
var proxy = require('proxy-middleware');

var paths = {
  scripts: 'app/js/**/*.js',
  htmls: ['app/index.html'],
  css: ['app/css/**/*.css', 'node_modules/bootstrap/dist/css/**/*.css']
};

gulp.task('clean', function() {
  return del(['dist']);
});

gulp.task('js', function () {
  return browserify({entries: './app/js/app.js', debug: true, transform: [babelify]})
      .bundle()
      .pipe(source('bundle.js'))
      .pipe(gulp.dest('dist/js'))
      .pipe(connect.reload());
});

gulp.task('html', function () {
  return gulp.src(paths.htmls)
    .pipe(gulp.dest('dist'))
    .pipe(connect.reload());
});

gulp.task('css', function () {
  return gulp.src(paths.css)
      .pipe(gulp.dest('dist/css'))
      .pipe(connect.reload());
});

gulp.task('bower', function () {
  return gulp.src('app/bower_components/**/*')
    .pipe(gulp.dest('dist/bower_components'));
});

gulp.task('connect', function () {
  return connect.server({
    root: 'dist',
    port: 8080,
    livereload: true,
    middleware: function () {
      function createProxy(path) {
        return proxy({
          port: 9000,
          pathname: path,
          route: path
        });
      }
      return [createProxy('/api')];
    }
  });
});

gulp.task('watch', function () {
  gulp.watch(paths.htmls, ['html']);
  gulp.watch(paths.css, ['css']);
  gulp.watch(paths.scripts, ['js']);
});

gulp.task('build', ['bower', 'js', 'css', 'html']);
gulp.task('dist', function (callback) {
  runSequence('clean', 'build', callback);
});
gulp.task('server', ['build', 'connect', 'watch']);
gulp.task('default', ['server']);
