(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .factory('PaiementSearch', PaiementSearch);

    PaiementSearch.$inject = ['$resource'];

    function PaiementSearch($resource) {
        var resourceUrl =  'api/_search/paiements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
