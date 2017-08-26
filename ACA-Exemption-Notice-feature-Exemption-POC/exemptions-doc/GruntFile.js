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
                    to: '&&'
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
                    'release/forms.min.js': ['../exemptions/ui.apps/src/main/content/jcr_root/etc/designs/cmsexemptions/clientlib-formtheme/js/forms.js']
                }
            }
        },
        pkg: grunt.file.readJSON('package.json'),
        concat: {
            options: {
                stripBanners: true
            },
            dist: {
                src: ['src/header.txt', 'release/forms.min.js', 'src/footer.txt'],
                dest: 'release/forms.xdp'
            }
        },
        jsdoc : {
            dist : {
                src: ['../exemptions/ui.apps/src/main/content/jcr_root/etc/designs/cmsexemptions/clientlib-formtheme/js/*.js'],
                options: {
                    private:true,
                    destination: 'docs'
                }
            }
        },
        compress: {
          exemption_schema_zip: {
            options: {
              archive: 'release/exemptions_schema.zip'
            },
            files: [
              {expand:true, cwd: '../Forms/Schema', src: ['./*.xsd'], dest: '/', flatten:true, filter: 'isFile'}, // includes files in path
            ]
          },
          xdp_zip: {
            options: {
              archive: 'release/forms_xdp.zip'
            },
            files: [
              {expand:true, cwd: '../Forms/XDPs', src: ['./*.xdp'], dest: '/', flatten:true, filter: 'isFile'}, // includes files in path
            ]
          }

        }
    });

    grunt.loadNpmTasks('grunt-contrib-compress');
    grunt.registerTask('default', ['uglify:dev', 'replace:dev', 'concat', 'jsdoc','compress']);

};
