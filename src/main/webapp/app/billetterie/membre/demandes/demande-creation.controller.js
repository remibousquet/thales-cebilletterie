(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('DemandeCreationController', DemandesController);

    DemandeCreationController.$inject = ['$scope', '$state', 'Demande', 'AlertService'];

    function DemandeCreationController ($scope, $state, Demande, AlertService) {
        var vm = this;
        
    }
})();
