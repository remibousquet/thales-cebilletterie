(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('paiement', {
            parent: 'entity',
            url: '/paiement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.paiement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/paiement/paiements.html',
                    controller: 'PaiementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paiement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('paiement-detail', {
            parent: 'entity',
            url: '/paiement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.paiement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/paiement/paiement-detail.html',
                    controller: 'PaiementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paiement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Paiement', function($stateParams, Paiement) {
                    return Paiement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'paiement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('paiement-detail.edit', {
            parent: 'paiement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paiement/paiement-dialog.html',
                    controller: 'PaiementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Paiement', function(Paiement) {
                            return Paiement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('paiement.new', {
            parent: 'paiement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paiement/paiement-dialog.html',
                    controller: 'PaiementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                banque: null,
                                numeroCheque: null,
                                montant: null,
                                commentaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('paiement', null, { reload: true });
                }, function() {
                    $state.go('paiement');
                });
            }]
        })
        .state('paiement.edit', {
            parent: 'paiement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paiement/paiement-dialog.html',
                    controller: 'PaiementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Paiement', function(Paiement) {
                            return Paiement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('paiement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('paiement.delete', {
            parent: 'paiement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paiement/paiement-delete-dialog.html',
                    controller: 'PaiementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Paiement', function(Paiement) {
                            return Paiement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('paiement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
