(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('StatutDemandeDialogController', StatutDemandeDialogController);

    StatutDemandeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StatutDemande'];

    function StatutDemandeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StatutDemande) {
        var vm = this;

        vm.statutDemande = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.statutDemande.id !== null) {
                StatutDemande.update(vm.statutDemande, onSaveSuccess, onSaveError);
            } else {
                StatutDemande.save(vm.statutDemande, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cebilletterieApp:statutDemandeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
