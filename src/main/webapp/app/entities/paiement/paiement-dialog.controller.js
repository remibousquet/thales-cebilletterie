(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('PaiementDialogController', PaiementDialogController);

    PaiementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Paiement'];

    function PaiementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Paiement) {
        var vm = this;

        vm.paiement = entity;
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
            if (vm.paiement.id !== null) {
                Paiement.update(vm.paiement, onSaveSuccess, onSaveError);
            } else {
                Paiement.save(vm.paiement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cebilletterieApp:paiementUpdate', result);
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
