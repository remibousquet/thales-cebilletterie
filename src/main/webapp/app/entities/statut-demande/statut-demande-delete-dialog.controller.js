(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('StatutDemandeDeleteController',StatutDemandeDeleteController);

    StatutDemandeDeleteController.$inject = ['$uibModalInstance', 'entity', 'StatutDemande'];

    function StatutDemandeDeleteController($uibModalInstance, entity, StatutDemande) {
        var vm = this;

        vm.statutDemande = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StatutDemande.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
