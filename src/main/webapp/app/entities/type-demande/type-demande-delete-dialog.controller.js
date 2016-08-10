(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TypeDemandeDeleteController',TypeDemandeDeleteController);

    TypeDemandeDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeDemande'];

    function TypeDemandeDeleteController($uibModalInstance, entity, TypeDemande) {
        var vm = this;

        vm.typeDemande = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeDemande.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
