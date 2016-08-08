(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .factory('PermanenceSearch', PermanenceSearch);

    PermanenceSearch.$inject = ['$resource'];

    function PermanenceSearch($resource) {
        var resourceUrl =  'api/_search/permanences/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
