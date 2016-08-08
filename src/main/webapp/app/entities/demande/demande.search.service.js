(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .factory('DemandeSearch', DemandeSearch);

    DemandeSearch.$inject = ['$resource'];

    function DemandeSearch($resource) {
        var resourceUrl =  'api/_search/demandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
