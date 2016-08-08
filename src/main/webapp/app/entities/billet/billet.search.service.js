(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .factory('BilletSearch', BilletSearch);

    BilletSearch.$inject = ['$resource'];

    function BilletSearch($resource) {
        var resourceUrl =  'api/_search/billets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
