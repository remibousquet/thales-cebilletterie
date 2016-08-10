(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .factory('TypeDemandeSearch', TypeDemandeSearch);

    TypeDemandeSearch.$inject = ['$resource'];

    function TypeDemandeSearch($resource) {
        var resourceUrl =  'api/_search/type-demandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
