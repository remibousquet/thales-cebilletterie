'use strict';

describe('Controller Tests', function() {

    describe('Demande Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDemande, MockStatutDemande, MockPaiement, MockBillet;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDemande = jasmine.createSpy('MockDemande');
            MockStatutDemande = jasmine.createSpy('MockStatutDemande');
            MockPaiement = jasmine.createSpy('MockPaiement');
            MockBillet = jasmine.createSpy('MockBillet');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Demande': MockDemande,
                'StatutDemande': MockStatutDemande,
                'Paiement': MockPaiement,
                'Billet': MockBillet
            };
            createController = function() {
                $injector.get('$controller')("DemandeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cebilletterieApp:demandeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
