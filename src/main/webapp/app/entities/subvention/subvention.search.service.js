(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .factory('SubventionSearch', SubventionSearch);

    SubventionSearch.$inject = ['$resource'];

    function SubventionSearch($resource) {
        var resourceUrl =  'api/_search/subventions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
