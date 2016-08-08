(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .factory('TitreTypeBilletSearch', TitreTypeBilletSearch);

    TitreTypeBilletSearch.$inject = ['$resource'];

    function TitreTypeBilletSearch($resource) {
        var resourceUrl =  'api/_search/titre-type-billets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
