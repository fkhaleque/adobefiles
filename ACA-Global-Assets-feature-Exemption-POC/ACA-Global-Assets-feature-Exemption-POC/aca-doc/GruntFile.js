module.exports = function (grunt) {

    require('load-grunt-tasks')(grunt, {scope: 'devDependencies'});

    // Project configuration.
    grunt.initConfig({
        replace: {
            dev: {
                src: ['release/*.js'],
                overwrite: true,
                replacements: [{
                    from: '&',
                    to: '&amp;'
                }, {
                    from: '>',
                    to: '&gt;'
                }, {
                    from: '<',
                    to: '&lt;'
                }]
            },
            debug: {
                src: ['src/*.js'],
                overwrite: true,
                replacements: [{
                    from: '&amp;',
                    to: '&'
                }, {
                    from: '&gt;',
                    to: '>'
                }, {
                    from: '&lt;',
                    to: '<'
                }]
            }
        },
        uglify: {
            dev: {
                files: {
                    'release/marketplace.min.js': ['../global-assets/ui.apps/src/main/content/jcr_root/etc/designs/acaglobalassets/clientlib-common/js/marketplace.js']
                }
            }
        },
        pkg: grunt.file.readJSON('package.json'),
        concat: {
            options: {
                stripBanners: true
            },
            dist: {
                src: ['src/header.txt', 'release/marketplace.min.js', 'src/footer.txt'],
                dest: 'release/marketplace.xdp'
            }
        },
        jsdoc : {
            dist : {
                src: ['../global-assets/ui.apps/src/main/content/jcr_root/etc/designs/acaglobalassets/clientlib-common/js/*.js'],
                options: {
                    private:true,
                    destination: 'docs'
                }
            }
        },
    });

    grunt.registerTask('default', ['uglify:dev', 'replace:dev', 'concat', 'jsdoc']);

};