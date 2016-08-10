(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TypeDemandeDialogController', TypeDemandeDialogController);

    TypeDemandeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeDemande'];

    function TypeDemandeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeDemande) {
        var vm = this;

        vm.typeDemande = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.typeDemande.id !== null) {
                TypeDemande.update(vm.typeDemande, onSaveSuccess, onSaveError);
            } else {
                TypeDemande.save(vm.typeDemande, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cebilletterieApp:typeDemandeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
