function fn() {
    var env = karate.properties['env'];
    karate.log('karate.env system property is:', env);
    karate.configure("ssl",true);
    karate.configure('connectTimeout', 30000);
    karate.configure('readTimeout', 30000);

    if (env === 'undefined') {
        env = 'local'
    }

    var config = {
        baseUrl: 'http://localhost:8080/',
    };
    if (env === 'pcf') {
        config.baseUrl = 'https://customerdemo-api.apps.pcf-t02-we.rabobank.nl/'
    }
    karate.log('karate.config.baseUrl:', config.baseUrl);

    return config;
}
