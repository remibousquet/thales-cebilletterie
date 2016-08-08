(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .factory('EnfantSearch', EnfantSearch);

    EnfantSearch.$inject = ['$resource'];

    function EnfantSearch($resource) {
        var resourceUrl =  'api/_search/enfants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
