(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('billet', {
            parent: 'entity',
            url: '/billet',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.billet.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/billet/billets.html',
                    controller: 'BilletController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('billet');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('billet-detail', {
            parent: 'entity',
            url: '/billet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.billet.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/billet/billet-detail.html',
                    controller: 'BilletDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('billet');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Billet', function($stateParams, Billet) {
                    return Billet.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'billet',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('billet-detail.edit', {
            parent: 'billet-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billet/billet-dialog.html',
                    controller: 'BilletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Billet', function(Billet) {
                            return Billet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('billet.new', {
            parent: 'billet',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billet/billet-dialog.html',
                    controller: 'BilletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                titre: null,
                                typeDate: false,
                                dateDebut: null,
                                dateFin: null,
                                horaire: null,
                                lieu: null,
                                zoneSalle: null,
                                typePublic: null,
                                prixUnitaire: null,
                                quantiteStock: null,
                                commentaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('billet', null, { reload: true });
                }, function() {
                    $state.go('billet');
                });
            }]
        })
        .state('billet.edit', {
            parent: 'billet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billet/billet-dialog.html',
                    controller: 'BilletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Billet', function(Billet) {
                            return Billet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('billet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('billet.delete', {
            parent: 'billet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billet/billet-delete-dialog.html',
                    controller: 'BilletDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Billet', function(Billet) {
                            return Billet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('billet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
