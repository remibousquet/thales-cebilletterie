(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('DemandeDialogController', DemandeDialogController);

    DemandeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Demande', 'StatutDemande', 'Paiement', 'Billet', 'TypeDemande'];

    function DemandeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Demande, StatutDemande, Paiement, Billet, TypeDemande) {
        var vm = this;

        vm.demande = entity;
        vm.clear = clear;
        vm.save = save;
        vm.statutdemandes = StatutDemande.query();
        vm.paiements = Paiement.query();
        vm.billets = Billet.query();
        vm.typedemandes = TypeDemande.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.demande.id !== null) {
                Demande.update(vm.demande, onSaveSuccess, onSaveError);
            } else {
                Demande.save(vm.demande, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cebilletterieApp:demandeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
